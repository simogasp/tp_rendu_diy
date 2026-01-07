#!/usr/bin/env python3

"""
Script used to generate the package to be given to the student.
Things to adapt:
* default name for the package
* files to copy
* directories to copy
* name of the main files to studentify
"""

import argparse
import logging
import os
import shutil
import subprocess
import sys
import tempfile
import zipfile

logging.basicConfig(
    level=logging.INFO,
    format="[%(levelname)s] %(message)s"
)
logger = logging.getLogger()


def main(working_dir: str, archive_name: str, skip_cleaning: bool = False, skip_packaging: bool = False):

    if not os.path.exists(working_dir):
        logger.info(f"Creating working directory {working_dir}")
        os.makedirs(working_dir)

    dest_name = os.path.join(working_dir, archive_name)

    logger.info(f"Creating directory {dest_name}")
    os.mkdir(dest_name)

    files_to_copy = [
        "Makefile",
        "checkstyle.xml",
        "README.md"
    ]
    logger.info(f"Copying files to {dest_name}")
    for file in files_to_copy:
        shutil.copy(file, dest_name)

    logger.info(f"Copying directories in {dest_name}")
    # shutil.copytree(".vscode", os.path.join(dest_name, ".vscode"))
    shutil.copytree("data", os.path.join(dest_name, "data"))
    shutil.copytree("test", os.path.join(dest_name, "test"))
    shutil.copytree("src", os.path.join(dest_name, "src"))
    shutil.copytree("lib", os.path.join(dest_name, "lib"))

    studentify_dir = os.path.join(tempfile.gettempdir(), "tpt")
    # Define trusted repository URL as a constant
    STUDENTIFY_REPO = "https://github.com/simogasp/studentipy.git"
    
    logger.info(f"Cloning studentify.py to {studentify_dir}")
    # Use absolute path to git command and validate the directory path
    studentify_dir_abs = os.path.abspath(studentify_dir)
    subprocess.check_call([
        "/usr/bin/git", 
        "clone", 
        STUDENTIFY_REPO, 
        studentify_dir_abs
    ])

    files_to_studentify = ["src/renderer/core/light/Lighting.java",
                           "src/renderer/core/mesh/Mesh.java",
                           "src/renderer/core/mesh/Texture.java",
                           "src/renderer/core/rasterizer/Rasterizer.java",
                           "src/renderer/controller/Renderer.java",
                           "src/renderer/core/shader/DepthBuffer.java",
                           "src/renderer/core/shader/TextureShader.java",
                           "src/renderer/core/camera/Transformation.java",
                           "test/unit/controller/ShaderFactoryTest.java"]
    for file in files_to_studentify:
        file_path = os.path.join(dest_name, file)
        logger.info(f"Applying studentify to {file_path}")
        subprocess.check_call(
            ["python3", os.path.join(studentify_dir, "studentify.py"), file_path, "-o", file_path, "--force"])
        
    # remove Depth and Normal shader implementations
    files_to_remove = [
        "src/renderer/core/shader/DepthShader.java",
        "src/renderer/core/shader/NormalMapShader.java"
    ]
    for file in files_to_remove:
        file_path = os.path.join(dest_name, file)
        logger.info(f"Removing {file_path}")
        os.remove(file_path)

    logger.info("Removing studentify.py")
    shutil.rmtree(studentify_dir)

    if not skip_packaging:
        logger.info(f"Generating archive {archive_name}.zip in {working_dir}")
        with zipfile.ZipFile(os.path.join(working_dir, f"{archive_name}.zip"), "w", compression=zipfile.ZIP_BZIP2) as zip_file:
            for root, dirs, files in os.walk(dest_name):
                for file in files:
                    zip_file.write(os.path.join(root, file), os.path.relpath(os.path.join(root, file), dest_name))

    if skip_cleaning:
        return
    logger.info(f"Cleaning up {dest_name}")
    shutil.rmtree(dest_name)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Script used to generate the package to be given to the student.")
    parser.add_argument(
        "-v",
        "--verbosity",
        dest="verbosity",
        choices=["DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"],
        default="INFO",
        help="Set the logging verbosity level (choose from DEBUG, INFO, WARNING, ERROR, CRITICAL).",
    )
    parser.add_argument(
        "-w",
        "--working-dir",
        dest="working_dir",
        help="Set the working directory where the files will be created.",
    )
    parser.add_argument(
        "-a",
        "--archive-name",
        dest="archive_name",
        help="The name of the archive to be created. [default=tp_renderer_diy-v<version>]"
    )
    parser.add_argument(
        "--skip-cleaning",
        action='store_true',
        dest="skip_cleaning",
        help="Skip the cleaning step leaving the code available.",
    )
    parser.add_argument(
        "--skip-packaging",
        action='store_true',
        dest="skip_packaging",
        help="Skip the packaging step without generating the archive.",
    )

    args = parser.parse_args()

    if args.verbosity:
        logger.setLevel(args.verbosity)

    if args.working_dir:
        wdir = args.working_dir
    else:
        wdir = tempfile.gettempdir()

    if args.archive_name:
        zip_name = args.archive_name
    else:
        version = subprocess.check_output(["git", "describe", "--tags"]).decode("utf-8").strip()[1:]
        zip_name = f"tp_renderer_diy-v{version}"

    try:
        main(working_dir=wdir, archive_name=zip_name, skip_cleaning=args.skip_cleaning, skip_packaging=args.skip_packaging)
    except Exception as e:
        logger.exception(e)
        sys.exit(1)
