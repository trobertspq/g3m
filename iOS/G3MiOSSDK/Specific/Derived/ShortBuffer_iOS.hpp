//
//  ShortBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//

#ifndef __G3MiOSSDK__ShortBuffer_iOS__
#define __G3MiOSSDK__ShortBuffer_iOS__

#include <OpenGLES/ES2/gl.h>
#include "IShortBuffer.hpp"
#include "ILogger.hpp"

class ShortBuffer_iOS : public IShortBuffer {
private:
  const int _size;
  short*    _values;
  int       _timestamp;

  mutable GLuint    _indexBuffer; //IBO
  mutable int       _indexBufferTimeStamp;

public:
  ShortBuffer_iOS(int size) :
  _size(size),
  _timestamp(0),
  _indexBuffer(GL_INVALID_VALUE),
  _indexBufferTimeStamp(-1)
  {
    _values = new short[size];
    if (_values == NULL){
      ILogger::instance()->logError("Allocating error.");
    }
  }

  virtual ~ShortBuffer_iOS() {
    delete [] _values;
  }

  int size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  short get(int i) const {
    
    if (i < 0 || i > _size){
      ILogger::instance()->logError("Buffer Put error.");
    }
    
    return _values[i];
  }

  void put(int i, short value) {
    
    if (i < 0 || i > _size){
      ILogger::instance()->logError("Buffer Put error.");
    }
    
    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(int i, short value) {
    if (i < 0 || i > _size){
      ILogger::instance()->logError("Buffer Put error.");
    }
    _values[i] = value;
  }

  short* getPointer() const {
    return _values;
  }

  const std::string description() const;

  void bindAsIBOToGPU(){
    if (_indexBuffer == GL_INVALID_VALUE){
      glGenBuffers(1, &_indexBuffer);
    }

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _indexBuffer);

    if (_indexBufferTimeStamp != _timestamp){
      _indexBufferTimeStamp = _timestamp;

      short* index = getPointer();
      int iboSize = sizeof(short) * size();

      glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboSize, index, GL_STATIC_DRAW);
    }
  }
  
};


#endif
