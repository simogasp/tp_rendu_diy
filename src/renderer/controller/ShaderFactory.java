package renderer.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import renderer.model.shader.Shader;

@SuppressWarnings("unchecked")
public final class ShaderFactory {

    private static final Set<Class<? extends Shader>> shaderSet = new HashSet<>();

    private ShaderFactory() {
    }

    public static void init() {
        // Récupére les noms de fichiers
        String[] files = (new File("build/cls/renderer/model/shader/")).list();
        // Vérifions qu'ils implantent la bonne interface
        for (int i = 0; i < files.length; i++) {
            Class<? extends Shader> shader;
            if (files[i].endsWith("Shader.class")) {
                String classname = files[i].substring(0, files[i].length() - 6);
                try {
                    shader = (Class<Shader>) Class.forName(Shader.class.getPackage().getName() + "." + classname);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    shader = null;
                }
                if ((shader != null) && (!shader.isAssignableFrom(Shader.class))) {
                    shaderSet.add(shader);
                }
            }
        }
    }

    public static Set<Class<? extends Shader>> getShaderSet() {
        return shaderSet;
    }

    public static Optional<Shader> create(String shaderName) {
        for (Class<? extends Shader> class1 : shaderSet) {
            if (class1.getSimpleName().equals(shaderName)) {
                try {
                    return Optional.of(class1.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.empty();
    }
    
    public static String[] getShaderSetAsStringArray() {
        final String[] res = new String[shaderSet.size()];
        int i = 0;
        Iterator<Class<? extends Shader>> iterator = shaderSet.iterator();
        while (iterator.hasNext()) {
            res[i++] = iterator.next().getSimpleName();
        }
        return res;
    }
}
