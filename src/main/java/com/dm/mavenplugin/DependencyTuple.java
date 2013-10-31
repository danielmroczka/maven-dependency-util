/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.dm.mavenplugin;

import org.apache.maven.model.Dependency;

/**
 *
 * @author daniel
 */
public class DependencyTuple {
    public Dependency dependency1;
    public Dependency dependency2;

    public DependencyTuple(Dependency dependency1, Dependency dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }
}
