# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v2026.1]

### Added

- added a GUI to show and control the rendering process with different options and scenes.
- added `DepthShader` that render the object as depth map (not distributed in the student code)
- added  `NormalShader` that render the normals of the object (not distributed in the student code)
- added the class `CreateClassesDiagram` to generate the UML class diagram in plantUML format from the code
- added Ant build file `build.xml` to have a cross-platform build system (makefile is still there)
- added more junit tests
- added github actions ci to build, run unit tests, generate the student version, and generate the documentation automatically on each push and pull request

### Changed

- reorganized the code in sub-packages: shader, rasterizer, lighting, texture, transformation, gui
- extract the vertex rasterization from `rasterizeEdge()` in a separate method `rasterizeVertex()` (there is no need to uncomment the code now as the GUI enables to switch between wireframe and vertex rendering)
- `Vector` inherits from `Matrix`.
- improved the generateStudent.py script to better check that no special markers are left in the code before generating the student archive and that the files for students are different from the original ones.

### Fixed

- the light contribution in `Lighting.applyLights()` are now clamped in the range [0,1]

### Removed

- `Vector3` class, `Vector` is enough, no need for a separate class
- removed the exceptions in matrix and vector creation, now using `IllegalArgumentException` to check the arguments

## [v2025.1] - 2025-06-10

### Added

- added makefile
- add the calibration matrix
- added README.md
- junit tests

### Changed

- replaced all `/* */` inline comments
- reformated all files
- moved attributes at the beginning of the class
- changed the structure of the project

### Fixed

- fixed language warnings
- fixed a bug in matrix multiplication (it was only working for square matrices)
- sourceCoord in Scene was of size 3 instead of 4
- fixed a bug in the transposition, it was not copying the data correctly
- proper initialization of the matrices calibration and worldToCamera as an identity matrix
- fixed a potential bug when checking the dimensions of the matrices when creating a new matrix, now checking that both of them are strictly positive
