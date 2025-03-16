package com.statemachinedemo.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

/**
 * @date 2025-03-12 08:42:12
 */
@Component
public class StateMachineBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] stateMachineBeanNames = beanFactory.getBeanNamesForType(StateMachine.class);
        for (String beanName : stateMachineBeanNames) {
            BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
            beanDef.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        }
    }
}
