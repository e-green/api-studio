package lk.egreen.msa.studio.extender

import javassist.CtClass

/**
 * Created by dewmal on 7/3/16.
 */
interface ClassProcessor {

    boolean shouldProcess(CtClass aClass);

    void process(Class<?> processClass);

    void process(List<Class<?>> processClasses);
}
