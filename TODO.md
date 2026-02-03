# TODO

## Build

* [x] makefile should be enough but maybe Ant (more cross-platform, e.g. for windoze)?
* [X] add ci with github actions

## General

* [ ] add a LICENSE
* [x] add the package name, move everything inside `src/packagename` so that the classes are visible for the the unit tests.
* [x] maybe re-organize in other subpackages, shader, rasterizer, etc. for each hierarchy of classes
* [x] remove or better use exceptions / avoid using try/catch for simple things like creating a matrix/vertex, use `IllegalArgumentException` for checking the arguments
* [x] keep only matrix class, vector and vector3 can be matrices
* [X] add a `homogeneous()` method
* [x] make some methods return `this` to allow chaining (e.g. `normalize()`) --> be careful with the semantic (sometimes returning the value means that the object is not affected by the method)
* [x] add a DepthShader that render the object as depth map (useful for debugging)
* [x] add a NormalShader that render the normals of the object (useful for debugging)
* [x] make the UML class diagram and find a way to generate it automatically from the code (better if in plantUML format)
* [x] add instructions to the `README.md` on how to set up the project with visual studio code installing the pluging for java and the checkstyle
* [ ] write a test to check that the tags are always there

## Transformation

* [ ] maybe use a proper camera class so we can have a generic camera and different implementations (perspective, orthographic, etc.)

## Fragment

* [x] numAttributes in Fragment can be a constant and the underlying array can be a fixed size array -> easier to check boundaries
* [ ] attributes can be better represented as a struct instead of an array

## Rasterizer

* [ ] check why in `Rasterizer.rasterizeEdge()` there are some  cases in which f1 and f2 have the same position (just projection or something else?)
* [x] resync the `Rasterizer` and `PerspectiveCorrectRasterizer` rasterizeFace()

## Lighting

* [x] refactor the code creating a Light abstract class and then the others that extend it with a getLightContribution() method that actually computes the contribution of the specific type of light.

## Texture

* [ ] clarify the javadoc in `sample()` to deal with repetitive textures.

## Scene

* [ ] `Scene`  should have the list of lights that `Renderer` has access to, avoid rebuilding the list of lights inside `Renderer` with the getters of Scene

## bugfixes

* [ ] in `Renderer.interpolate2()` fixed the choice of x or y and add the corner case when the two points are the same (it should never happen though)

* [x] the light contribution in `Lighting.applyLights()` should always be in the range [0,1] (clamp it)

## GUI

* [x] implement a GUI, adding scene navigation, buttons or toggles to enable/disable shaders etc. instead of doing it sequentially in the code
