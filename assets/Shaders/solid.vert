/* Solid Color Vertex Shader */

/* Uses GLSL 3.0 Core */
#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;

out vec3 vertColor;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
    vertColor = color;
}
