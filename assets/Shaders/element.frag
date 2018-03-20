/* GUI Element Fragment Shader */
#version 430 core

layout(location = 2) uniform sampler2D mTexture;
layout(location = 3) uniform vec4 mColor;

in vec2 TexCoord;

out vec4 fragColor;

void main() {
    /* fragColor = mColor; */
    fragColor = texture(mTexture, TexCoord);
}
