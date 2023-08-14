package org.originit.et.util;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TemplateUtil {

    private static Configuration configuration;

    public synchronized static Configuration configuration() {
        if (configuration != null) {
            return configuration;
        }
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.22) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

        // Specify the source where the template files come from. Here I set a
        // plain directory for it, but non-file-system sources are possible too:
        cfg.setClassForTemplateLoading(TemplateUtil.class,"/template");

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration = cfg;
        return configuration;
    }


    public static String getTemplate(String template, Object model) {
        final StringWriter stringWriter = new StringWriter();
        try {
            Map<String, Object> root = new HashMap<>(1);
            root.put("model", model);
            configuration().getTemplate(template).process(root, stringWriter);
        } catch (TemplateException e) {
            log.error("模板异常, path: {}, msg: {}", template, e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("模板文件异常, path: {}, msg: {}", template, e.getMessage());
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }

}
