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

    /**
     * The set of Class in Shader Package that implements Shader abstract class.
     */
    private static final Set<Class<? extends Shader>> SHADER_SET = new HashSet<>();

    private ShaderFactory() {
    }

    /**
     * Initialize the shader factory : launch classes in shader package which
     * implements
     * Shader abstract class.
     */
    public static void init() {
        // Récupére les noms de fichiers
        String[] files = (new File("build/cls/renderer/model/shader/")).list();
        // Vérifions qu'ils implantent la bonne interface
        for (int i = 0; i < files.length; i++) {
            Class<? extends Shader> shader;
            if (files[i].endsWith(".class")) {
                String classname = files[i].substring(0, files[i].length() - 6);
                try {
                    shader = (Class<Shader>) Class.forName(Shader.class.getPackage().getName() + "." + classname);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    shader = null;
                }
                if ((shader != null) && (Shader.class.isAssignableFrom(shader))) {
                    SHADER_SET.add(shader);
                }
            }
        }
    }

    /**
     * Returns the set of Class in Shader Package that implements Shader abstract
     * class.
     *
     * @return a set
     */
    public static Set<Class<? extends Shader>> getShaderSet() {
        return SHADER_SET;
    }

    /**
     * Create a instance of the given shaderName and return it if it is a success.
     * If the shaderName isn't in the ShaderSet, returns a empty Optional.
     *
     * @param shaderName the name of the shader we want to instanciate.
     * @return a optional of Shader
     */
    public static Optional<Shader> create(String shaderName) {
        for (Class<? extends Shader> class1 : SHADER_SET) {
            if (class1.getSimpleName().equals(shaderName)) {
                try {
                    return Optional.of(class1.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the names of classes that implements Shader in a array.
     *
     * @return a array of String
     */
    public static String[] getShaderSetAsStringArray() {
        final String[] res = new String[SHADER_SET.size()];
        int i = 0;
        Iterator<Class<? extends Shader>> iterator = SHADER_SET.iterator();
        while (iterator.hasNext()) {
            res[i++] = iterator.next().getSimpleName();
        }
        return res;
    }
}
