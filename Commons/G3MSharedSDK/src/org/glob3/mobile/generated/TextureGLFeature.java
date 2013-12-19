package org.glob3.mobile.generated; 
public class TextureGLFeature extends GLColorGroupFeature
{
  private IGLTextureId _texID = null;

  public void dispose()
  {
    super.dispose();
  }


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning OVERLOAD

  public TextureGLFeature(IGLTextureId texID, IFloatBuffer texCoords, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor, boolean coordsTransformed, Vector2D translate, Vector2D scale, float rotationPointX, float rotationPointY, float angleInRadians)
  {
     super(GLFeatureID.GLF_TEXTURE, 4, blend, sFactor, dFactor);
     _texID = texID;
  
    GPUAttributeValueVec2Float value = new GPUAttributeValueVec2Float(texCoords, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.TEXTURE_COORDS, value, false);
  
    if (coordsTransformed)
    {
      _values.addUniformValue(GPUUniformKey.TRANSLATION_TEXTURE_COORDS, new GPUUniformValueVec2Float((float) translate._x, (float) translate._y), false);
  
      _values.addUniformValue(GPUUniformKey.SCALE_TEXTURE_COORDS, new GPUUniformValueVec2Float((float) scale._x, (float) scale._y), false);
  
      if (angleInRadians != 0.0)
      {
        _values.addUniformValue(GPUUniformKey.ROTATION_POINT_TEXTURE_COORDS, new GPUUniformValueVec2Float(rotationPointX, rotationPointY), false);
  
        _values.addUniformValue(GPUUniformKey.ROTATION_ANGLE_TEXTURE_COORDS, new GPUUniformValueFloat(angleInRadians), false);
      }
    }
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
    state.bindTexture(_texID);
  }
}