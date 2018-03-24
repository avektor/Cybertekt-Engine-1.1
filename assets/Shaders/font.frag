/* Distance Font Fragment Shader */
#version 430 core

uniform sampler2D mTexture;
layout(location = 3) uniform vec4 mColor = vec4(1, 1, 1, 1);

const float width = 0.5;

in vec2 texCoord;

out vec4 fragColor;

float contour(in float d, in float w) {
    return smoothstep(0.5 - w, w + 0.5, d);
}

float samp(in vec2 uv, float w) {
    return contour(texture2D(mTexture, uv).a, w);
}

void main() {
    vec2 uv = texCoord.xy;
    float dist = texture2D(mTexture, texCoord).a;
    float alpha = smoothstep(0.5 - width, width + 0.5, dist);
    vec2 duv = 0.5 * (dFdx(uv) + dFdy(uv));
    vec4 box = vec4(uv-duv, uv+duv);
    float asum = samp(box.xy, width) + samp(box.zw, width) + samp(box.xw, width) + samp(box.zy, width);
    alpha = (alpha + 0.6 * asum) / 3.0;
    fragColor = vec4(vec3(alpha), alpha) * mColor;
}
