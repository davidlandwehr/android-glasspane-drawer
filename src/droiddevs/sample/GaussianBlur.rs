#pragma version(1))
#pragma rs java_package_name(droiddevs.sample)

rs_allocation pixels;
float *gaussian;
uint32_t kernelsize;
uint32_t width;
uint32_t height;

uchar4 __attribute__((kernel)) blurhorizontal(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out = 0;
  out.a = 255;
  int32_t  dist = kernelsize;
  uint32_t kernellength = dist/2;
  
  // Calulate using the kernel
  uint32_t startKernel = 0; 
  int32_t px = x-kernellength;
  
  if (px<0) {
  	startKernel-=px;
  	dist+=px;
  	px=0;
  }		
  
  if (x+kernellength>=width) {
  	int32_t over = x+kernellength-width;
  	dist-=over+1;
  }
  
  
  float4 val = 0;
  float weight=0;
  uchar4* pp = (uchar4*)rsGetElementAt(pixels, px, y);
  for (int32_t c =0; c<dist; c++) {
  		uchar4* sp = pp+c;
  		float kv = *(gaussian+(startKernel+c));
  		weight+=kv;
  	  	val.r += (sp->r * (kv));
  	  	val.g += (sp->g * (kv));
  	  	val.b += (sp->b * (kv));
  } 
 
  out.r = (uchar)(val.r/weight);
  out.g = (uchar)(val.g/weight);
  out.b = (uchar)(val.b/weight);
  return out;
}

uchar4 __attribute__((kernel)) blurvertical(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out = 0;
  out.a = 255;
  int32_t  dist = kernelsize;
  uint32_t kernellength = dist/2;
  
  // Calulate using the kernel
  uint32_t startKernel = 0; 
  int32_t py = y-kernellength;
  
  if (py<0) {
  	startKernel-=py;
  	dist+=py;
  	py=0;
  }		
  
  if (y+kernellength>=height) {
  	int32_t over = y+kernellength-height;
  	dist-=over+1;
  }
  
  
  float4 val = 0;
  float weight = 0;
  uchar4* pp = (uchar4*)rsGetElementAt(pixels, x, py);
  for (int32_t c =0; c<dist; c++) {
  		uchar4* sp = (uchar4*)rsGetElementAt(pixels, x, py+c);
  		float kv = *(gaussian+(startKernel+c));
  		weight += kv;
  	  	val.r += (sp->r * (kv));
  	  	val.g += (sp->g * (kv));
  	  	val.b += (sp->b * (kv));
  } 
 
  out.r = (uchar)(val.r/weight);
  out.g = (uchar)(val.g/weight);
  out.b = (uchar)(val.b/weight);
  return out;
}
