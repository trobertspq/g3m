//
//  GPUTextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GPUTextureBuilder_hpp
#define G3MiOSSDK_GPUTextureBuilder_hpp

#include "TextureBuilder.hpp"

class RenderContext;
class MutableMatrix44D;
class Camera;


class GPUTextureBuilder:public TextureBuilder
{
private:
  FBOContext        _fboContext;
  int               _defaultViewport[4];
  MutableMatrix44D  _projectionMatrix;

  
  void renderDummyImageInFBO(GL *gl) const;
  
  //void renderImageInFBO(const RenderContext* rc, const IImage* image) const;
  
  int startRenderFBO(GL *gl, Camera* camera, unsigned int width, unsigned int height);
  
  void stopRenderFBO(GL *gl) const;

  
public:
  int createTextureFromImages(const RenderContext* rc, 
                              const std::vector<const IImage*>& vImages, 
                              int width, int height) const;
  
  int createTextureFromImages(const RenderContext* rc,
                              const std::vector<const IImage*>& vImages, 
                              const std::vector<const Rectangle*>& vRectangles, 
                              int width, int height);
  
  void initialize(const InitializationContext* ic);  
};


#endif
