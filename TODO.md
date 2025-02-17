# TODO

## Build

* [ ] makefile should be enough but maybe Ant (more cross-platform, e.g. for windoze)?
* [ ] add ci with github actions

## General

* [ ] add a LICENSE
* [x] add the package name, move everything inside `src/packagename` so that the classes are visible for the the unit tests.
* [x] remove or better use exceptions / avoid using try/catch for simple things like creating a matrix/vertex, use `IllegalArgumentException` for checking the arguments
* [ ] only matrix class, vector and vector3 can be matrices

## Deploy student

* [ ] write a test to check that the tags are always there

* [ ] maybe remove projection matrix as it is never used

## Fragment

* [ ] numAttributes in Fragment can be a constant and the underlying array can be a fixed size array -> easier to check boundaries
* [ ] attributes can be better represented as a struct instead of a array

## Lighting

* [ ] refactor the code creating a Light abstract class and then the others that extend it with a getLightContribution() method that actually computes the contribution of the specific type of light.
