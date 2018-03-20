/* Distance Font Fragment Shader */
#version 430 core

uniform sampler2D mTexture;
layout(location = 3) uniform vec4 mColor = vec4(1, 1, 1, 1);

in vec2 texCoord;

out vec4 fragColor;

float contour(in float d, in float w) {
    return smoothstep(0.5 - w, 0.5 + w, d);
}

float samp(in vec2 uv, float w) {
    return contour(texture2D(mTexture, uv).a, w);
}

float aastep(float threshold, float value) {
    float afWidth = 0.7 * length(vec2(dFdx(value), dFdy(value)));
    return smoothstep(threshold-afWidth, threshold+afWidth, value);
}

void main() {

    // Retrieve Distance Value From Texture //
    vec2 uv = texCoord.xy;
    float dist = texture2D(mTexture, uv).a;

    // Keep Outline Width Constant At Any Scale //
    float width = fwidth(dist);

    // Simple Sampling //
    // float alpha = smoothstep(0.5 - width, 0.5 + width, dist);

    // Super Sampling //
    float alpha = contour(dist, width);
    float dscale = 0.70710678118; // Half of 1/sqrt2 - this can be modified.
    vec2 duv = dscale * (dFdx(uv) + dFdy(uv));
    vec4 box = vec4(uv-duv, uv+duv);
    float asum = samp(box.xy, width) + samp(box.zw, width) + samp(box.xw, width) + samp(box.zy, width);
    alpha = (alpha + 0.5 * asum) / 3.0;

    // Text Coloring //
    fragColor = vec4(vec3(alpha), alpha) * mColor;
}
