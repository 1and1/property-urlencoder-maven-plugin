/*
 * Copyright 2012 1&1.
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
package net.oneandone.maven.propertyurlencoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;

/**
 *
 * @author Mirko Friedenhagen
 */
@Component( role = AbstractMavenLifecycleParticipant.class, hint = "propertyUrlEncoder")
public class PropertyUrlEncoderLifeCycleParticipant extends AbstractMavenLifecycleParticipant {

    @Override
    public void afterSessionStart(MavenSession session) throws MavenExecutionException {
        super.afterSessionStart(session);
        System.out.println("XXXX session = " + session);
        final List<MavenProject> projects = session.getProjects();
        for (final MavenProject project : projects) {
            final Properties properties = project.getProperties();
            final String qaTicketUrl = properties.getProperty("qa-ticket-url", "NONE");
            final String encodedValue;
            try {
                encodedValue = URLEncoder.encode(qaTicketUrl, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            properties.setProperty("qa-ticket-url.lcenc", encodedValue);
        }
    }

    @Override
    public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
        super.afterProjectsRead(session);
        System.out.println("XXXX session = " + session);
        final List<MavenProject> projects = session.getProjects();
        for (final MavenProject project : projects) {
            final Properties properties = project.getProperties();
            final String qaTicketUrl = properties.getProperty("qa-ticket-url", "NONE");
            final String encodedValue;
            try {
                encodedValue = URLEncoder.encode(qaTicketUrl, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            properties.setProperty("qa-ticket-url.lcenc", encodedValue);
        }
    }
}
