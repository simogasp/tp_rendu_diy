# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

### Removed
