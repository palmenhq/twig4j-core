package org.twig.compiler;

import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;
import org.twig.template.Template;

public class RuntimeTemplateCompiler {
    private CachedCompiler cachedCompiler = CompilerUtils.CACHED_COMPILER;

    public RuntimeTemplateCompiler() {
    }

    public RuntimeTemplateCompiler(CachedCompiler cachedCompiler) {
        this.cachedCompiler = cachedCompiler;
    }

    public Template compile(String sourceCode, String name) {
        try {
            Template template = (Template) cachedCompiler.loadFromJava(name, sourceCode).newInstance();

            return template;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Mismatching class name and template class name when runtime compiling template");
        } catch (InstantiationException e) {
            throw new RuntimeException("InstantiationException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException: " + e.getMessage());
        }
    }

    public void setCachedCompiler(CachedCompiler cachedCompiler) {
        this.cachedCompiler = cachedCompiler;
    }
}
