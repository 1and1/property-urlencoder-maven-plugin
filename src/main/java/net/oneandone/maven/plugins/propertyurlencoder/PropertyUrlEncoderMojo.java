package net.oneandone.maven.plugins.propertyurlencoder;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.util.StringUtils;

/**
 * Goal which adds the url-encoded form of properties to the project.
 *
 * @goal encode
 *
 * @phase validate
 */
public class PropertyUrlEncoderMojo extends AbstractMojo {

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${session}"
     * @readonly
     * @required
     */
    protected MavenSession session;

    /**
     * @parameter expression="${mojoExecution}"
     */
    private org.apache.maven.plugin.MojoExecution execution;

    /**
     * Comma separated list of properties to encode.
     *
     * @parameter expression="${property-url-encoder.properties}"
     *
     * @required
     */
    private String properties;

    /**
     * Suffix to append to the urlencoded properties
     *
     * @parameter default-value=".urlencoded" expression="${property-url-encoder.suffix}"
     *
     */
    private String suffix;

    @Override
    public void execute() throws MojoExecutionException {
        final List<String> propertyNames = Arrays.asList(StringUtils.stripAll(StringUtils.split(properties, ",")));
        final PluginParameterExpressionEvaluator evaluator = new PluginParameterExpressionEvaluator(session, execution);
        for (final String propertyName : propertyNames) {
            final String propertyValue;
            try {
                getLog().debug("propertyName=" + propertyName);
                propertyValue = (String) evaluator.evaluate("${" + propertyName + "}");
            } catch (ExpressionEvaluationException ex) {
                throw new RuntimeException(ex);
            }
            final String encodedValue;
            try {
                encodedValue = URLEncoder.encode(propertyValue, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            final String nameOfEncodedProperty = propertyName + suffix;
            project.getProperties().put(nameOfEncodedProperty, encodedValue);
            getLog().debug(String.format("%s=%s", nameOfEncodedProperty, encodedValue));
        }


    }
}
