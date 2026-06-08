package renderer.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.Arrays;
import renderer.core.shader.FragmentShader;

@SuppressWarnings("unchecked")
public final class ShaderFactory {

    /**
     * The set of Class in Shader Package that implements Shader abstract class.
     */
    private static final Set<Class<? extends FragmentShader>> SHADER_SET = new HashSet<>();

    private ShaderFactory() {
    }

    /**
     * Initialize the shader factory : launch classes in shader package which
     * implements
     * Shader abstract class.
     */
    public static void init() {
        // Get the classloader
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // scan both the pipeline package and the legacy shader package where
        // FragmentShader implementations may live
        final List<String> packageNames = Arrays.asList(
                FragmentShader.class.getPackage().getName(),
                "renderer.core.shader");

        for (String packageName : packageNames) {
            final String path = packageName.replace('.', '/');
            Enumeration<URL> resources = null;
            try {
                resources = classLoader.getResources(path);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                try {
                    final URI uri = resource.toURI();
                    final File dir = new File(uri);
                    dirs.add(dir);
                    System.out.println("Path: " + dir.getAbsolutePath());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(
                        "Failed to convert shader resource URL to URI: " + resource, e);
                }
            }

            for (File dir : dirs) {
                if (dir == null || !dir.isDirectory()) continue;
                System.out.println("Scanning " + packageName + " for FragmentShader implementations...");
                final String[] files = dir.list();
                if (files == null) continue;
                for (String file : files) {
                    if (file == null || !file.endsWith(".class")) {
                        continue;
                    }
                    final String classname = file.substring(0, file.lastIndexOf('.'));
                    try {
                        final String fullClassName = packageName + "." + classname;
                        final Class<?> loaded = Class.forName(fullClassName);
                        if (FragmentShader.class.isAssignableFrom(loaded)) {
                            // Skip interfaces and abstract classes (including the FragmentShader interface itself)
                            final int mods = loaded.getModifiers();
                            if (!loaded.isInterface()
                                    && !java.lang.reflect.Modifier.isAbstract(mods)
                                    && !loaded.equals(FragmentShader.class)) {
                                @SuppressWarnings("unchecked")
                                final Class<? extends FragmentShader> shaderClass = (Class<? extends FragmentShader>) loaded;
                                SHADER_SET.add(shaderClass);
                                System.out.println("Found FragmentShader implementation: " + classname);
                            } else {
                                System.out.println("Skipping abstract/interface or base FragmentShader: " + classname);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
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
    public static Set<Class<? extends FragmentShader>> getShaderSet() {
        return SHADER_SET;
    }

    /**
     * Create a instance of the given shaderName and return it if it is a success.
     * If the shaderName isn't in the ShaderSet, returns a empty Optional.
     *
     * @param shaderName the name of the shader we want to instantiate.
     * @return a optional of Shader
     */
    public static Optional<FragmentShader> create(String shaderName) {
        for (Class<? extends FragmentShader> class1 : SHADER_SET) {
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
        Iterator<Class<? extends FragmentShader>> iterator = SHADER_SET.iterator();
        while (iterator.hasNext()) {
            res[i++] = iterator.next().getSimpleName();
        }
        return res;
    }
}
