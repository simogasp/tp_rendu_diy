# This file contains code that was changed, in case it's decided to switch back

### This is the code that was in Rasterizer, instead of the call to interpolate3 :

    // Interpolate the depth
    Vector vecAtt = new Vector(v1.depth, v2.depth, v3.depth);
    double interpolated = bar.dot(vecAtt);
    fragment.setAttribute(Fragment.DEPTH, interpolated);

    // Interpolate the normal
    Vector n1 = v1.normal;
    Vector n2 = v2.normal;
    Vector n3 = v3.normal;

    for(int i = 0 ; i < 3 ; i++) {
        vecAtt = new Vector(n1.get(i), n2.get(i), n3.get(i));
        interpolated = bar.dot(vecAtt);
        fragment.setAttribute(Fragment.NORMAL_X + i, interpolated);
    }

    // Interpolate the world position
    Vector wp1 = v1.worldPosition;
    Vector wp2 = v2.worldPosition;
    Vector wp3 = v3.worldPosition;

    for(int i = 0 ; i < 3 ; i++) {
        vecAtt = new Vector(wp1.get(i), wp2.get(i), wp3.get(i));
        interpolated = bar.dot(vecAtt);
        fragment.setAttribute(Fragment.WORLD_X + i, interpolated);
    }

    // Interpolate the color
    double[] c1 = v1.color;
    double[] c2 = v2.color;
    double[] c3 = v3.color;

    for(int i = 0 ; i < 3 ; i++) {
        vecAtt = new Vector(c1[i], c2[i], c3[i]);
        interpolated = MathUtils.clamp(bar.dot(vecAtt), 0, 1);
        fragment.setAttribute(Fragment.COLOR_R + i, interpolated);
    }

    // Interpolate the alpha value
    vecAtt = new Vector(v1.alpha, v2.alpha, v3.alpha);
    interpolated = MathUtils.clamp(bar.dot(vecAtt), 0, 1);
    fragment.setAttribute(Fragment.COLOR_ALPHA, interpolated);

    // Interpolate the UV coordinates
    vecAtt = new Vector(v1.u, v2.u, v3.u);
    interpolated = bar.dot(vecAtt);
    fragment.setAttribute(Fragment.TEXTURE_U, interpolated);
    vecAtt = new Vector(v1.v, v2.v, v3.v);
    interpolated = bar.dot(vecAtt);
    fragment.setAttribute(Fragment.TEXTURE_V, interpolated);



### This was the code in PerspectiveCorrectRasterizer, instead of the call to interpolate3 :

    // Interpolate the depth
    double a1 = v1.depth;
    double a2 = v2.depth;
    double a3 = v3.depth;

    double aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
    double interpolated = aOverZ / oneOverZ;
    fragment.setAttribute(Fragment.DEPTH, interpolated);

    // Interpolate the normal
    Vector n1 = v1.normal;
    Vector n2 = v2.normal;
    Vector n3 = v3.normal;

    for(int i = 0 ; i < 3 ; i++) {
        a1 = n1.get(i);
        a2 = n2.get(i);
        a3 = n3.get(i);
        aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
        interpolated = aOverZ / oneOverZ;
        fragment.setAttribute(Fragment.NORMAL_X + i, interpolated);
    }

    // Interpolate the world position
    Vector wp1 = v1.normal;
    Vector wp2 = v2.normal;
    Vector wp3 = v3.normal;

    for(int i = 0 ; i < 3 ; i++) {
        a1 = wp1.get(i);
        a2 = wp2.get(i);
        a3 = wp3.get(i);
        aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
        interpolated = aOverZ / oneOverZ;
        fragment.setAttribute(Fragment.WORLD_X + i, interpolated);
    }

    // Interpolate the color
    double[] c1 = v1.color;
    double[] c2 = v2.color;
    double[] c3 = v3.color;

    for(int i = 0 ; i < 3 ; i++) {
        a1 = c1[i];
        a2 = c2[i];
        a3 = c3[i];
        aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
        interpolated = MathUtils.clamp(aOverZ / oneOverZ, 0, 1);
        fragment.setAttribute(Fragment.COLOR_R + i, interpolated);
    }

    // Interpolate the alpha value
    a1 = v1.alpha;
    a2 = v2.alpha;
    a3 = v3.alpha;

    aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
    interpolated = MathUtils.clamp(aOverZ / oneOverZ, 0, 1);
    fragment.setAttribute(Fragment.COLOR_ALPHA, interpolated);

    // Interpolate the UV coordinates
    a1 = v1.u;
    a2 = v2.u;
    a3 = v3.u;

    aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
    interpolated = aOverZ / oneOverZ;
    fragment.setAttribute(Fragment.TEXTURE_U, interpolated);

    a1 = v1.v;
    a2 = v2.v;
    a3 = v3.v;

    aOverZ = w1 * a1 + w2 * a2 + w3 * a3;
    interpolated = aOverZ / oneOverZ;
    fragment.setAttribute(Fragment.TEXTURE_V, interpolated);

