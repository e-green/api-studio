package lk.egreen.msa.studio.extender

import javassist.CtClass
import org.osgi.framework.Bundle

/**
 * Created by dewmal on 7/3/16.
 */
interface ClassProcessor {

    boolean shouldProcess(CtClass aClass);

    void process(Class<?> processClass, Dictionary<?, ?> dictionary);

    void process(Set<Class<?>> processClasses, Dictionary<?, ?> dictionary, Bundle bundle);
}
