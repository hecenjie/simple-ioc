package cn.hecenjie.summerioc.beans.factory.xml;

import cn.hecenjie.summerioc.beans.factory.BeansException;
import cn.hecenjie.summerioc.beans.factory.support.AbstractBeanDefinitionReader;
import cn.hecenjie.summerioc.beans.factory.support.BeanDefinitionRegistry;
import cn.hecenjie.summerioc.core.io.Resource;
import cn.hecenjie.summerioc.core.io.support.EncodedResource;
import org.w3c.dom.Document;
import org.xml.sax.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    /** 禁用验证模式 */
    public static final int VALIDATION_NONE = 0;

    /** 自动获取验证模式 */
    public static final int VALIDATION_AUTO = 1;

    /** DTD 验证模式 */
    public static final int VALIDATION_DTD = 2;

    /** XSD 验证模式 */
    public static final int VALIDATION_XSD = 3;

    /** 默认为自动模式 */
    private int validationMode = VALIDATION_AUTO;

    /** 当前线程正在加载的 EncodedResource 集合 */
    private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded = new ThreadLocal<>();

    private DocumentLoader documentLoader = new DefaultDocumentLoader();

    // todo: 以下这一块还没实现，目前都为null，不过不影响
    private EntityResolver entityResolver;  // 提供自定义如何寻找验证文件的逻辑（默认是从网络上下载）
    private boolean namespaceAware = false; // 命名空间的支持
    private ErrorHandler errorHandler;      // 解析时的错误处理，可以获取到哪个元素有错


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void setValidationMode(int validationMode) {
        this.validationMode = validationMode;
    }

    public int getValidationMode() {
        return this.validationMode;
    }

    public int loadBeanDefinitions(Resource resource) throws BeansException {
        //包装成 EncodedResource，可以指定解析文件时使用的字符集和编码，保证内容读取的正确性
        return loadBeanDefinitions(new EncodedResource(resource));
    }

    /**
     * 从指定的 XML 文件中加载 BeanDefinition，该方法过程如下：
     * 1. 获取已加载的资源，并将当前资源加入 resourcesCurrentlyBeingLoaded
     * 2. 真正的加载
     *    2.1 根据xml文件获取 Document 实例
     *    2.2 根据获取的 Document 实例，注册 BeanDefinition
     * 3. 将当前资源从 resourcesCurrentlyBeingLoaded 移除
     *
     * @param encodedResource 由 EncodedResource 包装后的 Resource
     * @return BeanDefinition 的个数
     * @throws BeansException
     */
    public int loadBeanDefinitions(EncodedResource encodedResource) throws BeansException {
        logger.info("Loading XML bean definitions from " + encodedResource);

        // 获取已经加载过的资源，如果没有则初始化
        Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
        if (currentResources == null) {
            currentResources = new HashSet<>(4);
            this.resourcesCurrentlyBeingLoaded.set(currentResources);
        }
        if (!currentResources.add(encodedResource)) {	// 将当前资源记录到缓存中，如果已存在，抛出异常
            throw new BeansException(
                    "Detected cyclic loading of " + encodedResource + " - check your import definitions!");
        }

        try {
            // 从 EncodedResource 获取其中的 InputStream
            InputStream inputStream = encodedResource.getInputStream();
            try {
                InputSource inputSource = new InputSource(inputStream); // 构造一个代表 XML 的 InputSource
                if (encodedResource.getEncoding() != null) {
                    inputSource.setEncoding(encodedResource.getEncoding());
                }
                return doLoadBeanDefinitions(inputSource, encodedResource.getResource());	// 真正的加载实现
            } finally {
                inputStream.close();
            }
        } catch (IOException ex) {
            throw new BeansException(
                    "IOException parsing XML document from " + encodedResource.getResource(), ex);
        } finally {
            // 加载完成（或失败），将该资源从当前线程正在加载的资源缓存中删除
            currentResources.remove(encodedResource);
            if (currentResources.isEmpty()) {
                this.resourcesCurrentlyBeingLoaded.remove();
            }
        }
    }

    /**
     * 从指定的 XML 文件中加载 BeanDefinition 的真正实现
     * @param inputSource 包装了{@link InputStream)，表示一个 xml 实体
     * @param resource xml文件的抽象
     * @return 注册的 BeanDefinition 的数量
     */
    private int doLoadBeanDefinitions(InputSource inputSource, Resource resource) throws BeansException{
        Document doc = null;
        try {
            // 根据xml文件，获取 Document 实例
            doc = doLoadDocument(inputSource, resource);
        } catch (Exception e) {
            throw new BeansException("Fail to parse to document by resource");
        }
        // 根据获取的 Document 实例，注册 BeanDefinition
        return registerBeanDefinitions(doc, resource);
    }

    /**
     * 根据xml文件，获取{@link Document}实例
     * @param inputSource 包装了{@link InputStream)，表示一个 xml 实体
     * @param resource XML文件的抽象
     * @return {@link Document}
     * @throws Exception
     */
    private Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
        return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
                getValidationModeForResource(resource), isNamespaceAware());
    }

    /**
     * 根据获取的{@link Document}实例，注册 BeanDefinition
     * @param doc {@link Document}
     * @param resource XML文件的抽象
     * @return 注册的 BeanDefinition 的数量
     * @throws BeansException
     */
    private int registerBeanDefinitions(Document doc, Resource resource) throws BeansException {
        // 创建 BeanDefinitionDocumentReader 对象
        BeanDefinitionDocumentReader documentReader = new DefaultBeanDefinitionDocumentReader();
        // 获取已注册的 BeanDefinition 数量
        int countBefore = getRegistry().getBeanDefinitionCount();
        // 注册 BeanDefinition
        documentReader.registerBeanDefinitions(doc, getRegistry());
        // 计算新注册的 BeanDefinition 数量
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }


    private int getValidationModeForResource(Resource resource) {
        int validationModeToUse = getValidationMode();		// 获取指定的验证模式，默认为自动模式
        if (validationModeToUse != VALIDATION_AUTO) {		// 如果不为默认的自动模式，说明手动指定了，那么直接返回
            return validationModeToUse;
        }
        // 否则，使用XML探测器探测验证模式，目前该方法的实现是直接返回 XSD 验证模式
        int detectedMode = detectValidationMode(resource);
        if (detectedMode != VALIDATION_AUTO) {	// 如果探测出的验证模式不是自动的，那么直接返回探测出的验证模式即可
            return detectedMode;
        }
        // 否则，返回XSD验证模式
        return VALIDATION_XSD;
    }

    /**
     * 探测使用哪种验证模式，这里直接返回了XSD验证模式，实际上 Spring 对此的实现要复杂许多
     * @param resource
     * @return
     */
    private int detectValidationMode(Resource resource) {
        return VALIDATION_XSD;
    }


    // todo: 以下这一块还没实现，目前都返回null，不过不影响

    private EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    private boolean isNamespaceAware() {
        return this.namespaceAware;
    }
}
