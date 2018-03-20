/* Textured Fragment Shader */

/* Uses GLSL 3.0 Core */
#version 330 core

uniform sampler2D texture_sampler;

in vec2 outTexCoords;
out vec4 fragColor;

void main() {
    fragColor = texture(texture_sampler, outTexCoords);
}
