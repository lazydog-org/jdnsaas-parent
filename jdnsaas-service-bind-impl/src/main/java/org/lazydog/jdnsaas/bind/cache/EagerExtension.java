/**
 * Copyright 2013 lazydog.org.
 *
 * This file is part of JDNSaaS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lazydog.jdnsaas.bind.cache;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eager extension.
 * 
 * @author  Ron Rickard
 */
public class EagerExtension implements Extension {
    
    private static final Logger logger = LoggerFactory.getLogger(EagerExtension.class);
    private List<Bean<?>> eagerBeans = new ArrayList<Bean<?>>();
 
    /**
     * Process bean.
     * 
     * @param  event  the process bean event.
     */
    public <T> void processBean(@Observes ProcessBean<T> event) {

        // Check if the bean is annotated with @Eager and @ApplicationScoped.
        if (event.getAnnotated().isAnnotationPresent(Eager.class) && event.getAnnotated().isAnnotationPresent(ApplicationScoped.class)) {

            // Add the bean to the eager beans.
            this.eagerBeans.add(event.getBean());
            logger.debug("Found eager managed bean {}.", event.getBean().getBeanClass().getName());
        }
    }
 
    /**
     * After deployment validation.
     * 
     * @param  event        the after deployment validation event.
     * @param  beanManager  the bean manager.
     */
    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        
        // Loop through the eager beans.
        for (Bean<?> eagerBean : this.eagerBeans) {
            
            // Initialize the eager beans.  Note: the toString() is important to initialize the bean.
            beanManager.getReference(eagerBean, eagerBean.getBeanClass(), beanManager.createCreationalContext(eagerBean)).toString();
            logger.debug("Initialized eager managed bean {}.", eagerBean.getBeanClass().getName());
        }
    }
}
