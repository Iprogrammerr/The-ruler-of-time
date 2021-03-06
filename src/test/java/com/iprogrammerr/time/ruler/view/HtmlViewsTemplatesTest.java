package com.iprogrammerr.time.ruler.view;

import com.iprogrammerr.time.ruler.matcher.ThrowsMatcher;
import com.iprogrammerr.time.ruler.setup.TestTemplatesSetup;
import com.iprogrammerr.time.ruler.tool.RandomStrings;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

public class HtmlViewsTemplatesTest {

    private static final int TEMPLATE_TITLE_SIZE = 10;
    private static final String TEMPLATE_TITLE = "appName";
    private static final String TEMPLATE_TEXT = "invitation";
    private static final String TEMPLATE = "index";
    private final TestTemplatesSetup setup = new TestTemplatesSetup();

    @Test
    public void rendersWithParamsView() {
        RandomStrings randomStrings = new RandomStrings();
        Map<String, Object> params = new HashMap<>();
        params.put(TEMPLATE_TITLE, randomStrings.alphabetic(TEMPLATE_TITLE_SIZE));
        params.put(TEMPLATE_TEXT, randomStrings.alphabetic());
        Context context = new Context();
        context.setVariables(params);
        TemplateEngine engine = setup.engine();
        String rendered = engine.process(TEMPLATE + ".html", context);
        HtmlViewsTemplates viewsTemplates = new HtmlViewsTemplates(engine);
        MatcherAssert.assertThat("Does not render with params view", viewsTemplates.rendered(TEMPLATE, params),
            Matchers.equalTo(rendered));
    }

    @Test
    public void rendersView() {
        Context context = new Context();
        context.setVariables(new HashMap<>());
        TemplateEngine engine = setup.engine();
        String rendered = engine.process(TEMPLATE + ".html", context);
        HtmlViewsTemplates viewsTemplates = new HtmlViewsTemplates(engine);
        MatcherAssert.assertThat("Does not render view", viewsTemplates.rendered(TEMPLATE),
            Matchers.equalTo(rendered));
    }

    @Test
    public void throwsExceptionIfTemplateDoesNotExist() {
        HtmlViewsTemplates viewsTemplates = new HtmlViewsTemplates(setup.engine());
        String nonExistent = new RandomStrings().alphabetic(3);
        String path = nonExistent + ".html";
        MatcherAssert.assertThat(
            "Should throw exception with proper message",
            () -> viewsTemplates.rendered(nonExistent, new HashMap<>()),
            new ThrowsMatcher(path)
        );
    }
}
