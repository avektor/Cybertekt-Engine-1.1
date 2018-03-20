/* GUI Element Vertex Shader */
#version 430 core

layout(location = 0) uniform mat4 projectionMatrix;
layout(location = 1) uniform mat4 worldMatrix;

layout(location = 0) in vec3 vPosition;
layout(location = 1) in vec2 vTexCoord;

out vec2 TexCoord;

void main() {
    gl_Position = projectionMatrix * worldMatrix * vec4(vPosition, 1.0);
    TexCoord = vTexCoord;
}
