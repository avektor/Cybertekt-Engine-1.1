/* Texture Vertex Shader */

/* Uses GLSL 3.0 Core */
#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

layout(location=0) in vec3 position;
layout(location=2) in vec2 texCoords;

out vec2 outTexCoords;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
    outTexCoords = texCoords;
}
