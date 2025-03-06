# TODO

## Build

* [ ] makefile should be enough but maybe Ant (more cross-platform, e.g. for windoze)?
* [ ] add ci with github actions

## General

* [ ] add a LICENSE
* [x] add the package name, move everything inside `src/packagename` so that the classes are visible for the the unit tests.
* [x] remove or better use exceptions / avoid using try/catch for simple things like creating a matrix/vertex, use `IllegalArgumentException` for checking the arguments
* [ ] keep only matrix class, vector and vector3 can be matrices
* [ ] add a `homogeneous()` method
* [ ] make some methods return `this` to allow chaining (e.g. `normalize()`)
* [ ] add a NormalShader that render the normals of the object (useful for debugging)

## Deploy student

* [ ] write a test to check that the tags are always there
* [ ] maybe remove projection matrix as it is never used

## Fragment

* [ ] numAttributes in Fragment can be a constant and the underlying array can be a fixed size array -> easier to check boundaries
* [ ] attributes can be better represented as a struct instead of a array

## Rasterizer

* [ ] check why in `Rasterizer.rasterizeEdge()` there are some  cases in which f1 and f2 have the same position (just projection or something else?)

## Lighting

* [ ] refactor the code creating a Light abstract class and then the others that extend it with a getLightContribution() method that actually computes the contribution of the specific type of light.

## Texture

* [ ] clarify the javadoc in `sample()` to deal with repetitive textures.

## Scene

* [ ] `Scene`  should have the list of lights that `Renderer` has access to, avoid rebuilding the list of lights inside `Renderer` with the getters of Scene

## bugfixes

* [ ] in `Renderer.interpolate2()` fixed the choice of x or y and add the corner case when the two points are the same (it should never happen though)

* [ ] the light contribution in `Lighting.applyLights()` should always be in the range [0,1] (clamp it)
