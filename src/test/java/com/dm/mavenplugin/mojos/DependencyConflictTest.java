package com.dm.mavenplugin.mojos;

import com.dm.mavenplugin.mojos.DependencyConflict;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.model.Dependency;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author daniel
 */
public class DependencyConflictTest {
    
    @Test
    public void testConflictDetect() {
        DependencyConflict dep = new DependencyConflict();
        List<Dependency> list = new ArrayList<Dependency>();
        
        Dependency dep1 = new Dependency();
        dep1.setGroupId("group1");
        dep1.setArtifactId("artifact1");
        dep1.setVersion("1.0");
        list.add(dep1);

        Dependency dep2 = new Dependency();
        dep2.setGroupId("group2");
        dep2.setArtifactId("artifact2");
        dep1.setVersion("1.0");
        list.add(dep2);
        
        Dependency dep3 = new Dependency();
        dep3.setGroupId("group1");
        dep3.setArtifactId("artifact1");
        dep1.setVersion("1.1");
        list.add(dep3);
        
        assertEquals(1, dep.conflictDetect(list).size());
        assertFalse(dep.conflictDetect(list).get(0).dependency1.getVersion().equals(dep.conflictDetect(list).get(0).dependency2.getVersion()));
    }
}