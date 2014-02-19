package droiddevs.sample;

public class KernelUtil {
	public static float[] getGaussianKernel(int distance) {
		int dist = distance+distance+1;
    	float[] kernel = new float[dist];
    	float tau = dist/12f;
    	float tau2 = tau*tau*2;
    	float fac = (float)(1f / (Math.sqrt(tau2*Math.PI)));
    	
    	float sum = 0;
    	for (int i=0; i<=distance; i++) {
    		float x = (i-distance);
    		float val = 0;
    		// sampling
    		for (float sx=x-0.5f; sx<x+0.6; sx+=0.1f)
    			val += (float)(fac*Math.pow(Math.E, -sx*sx/tau2));
    		val /= 11;
    		kernel[i] = val;
    		kernel[dist-1-i] = val;
    		if (i<distance)
    			sum += val + val;
    		else
    			sum+=val;
    	}
    	
    	for (int i=0; i<dist; i++)
    		kernel[i] /= sum;
    
    	return kernel;
    }
}
