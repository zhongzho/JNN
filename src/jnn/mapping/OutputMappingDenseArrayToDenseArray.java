package jnn.mapping;

import jnn.functions.DenseArrayToDenseArrayTransform;
import jnn.functions.parametrized.Layer;
import jnn.neuron.DenseNeuronArray;
import jnn.neuron.NeuronArray;

public class OutputMappingDenseArrayToDenseArray extends Mapping{
	DenseNeuronArray[] input;
	DenseNeuronArray[] output;
	DenseArrayToDenseArrayTransform layer;

	public OutputMappingDenseArrayToDenseArray(int inputStart, int inputEnd,
			int outputStart, int outputEnd, DenseNeuronArray[] input,
			DenseNeuronArray[] output, DenseArrayToDenseArrayTransform layer) {
		super(inputStart, inputEnd, outputStart, outputEnd);
		this.input = input;
		this.output = output;
		this.layer = layer;
		validate();
		setTimedLayer(layer);
	}

	public OutputMappingDenseArrayToDenseArray(DenseNeuronArray[] input,
			DenseNeuronArray[] output, DenseArrayToDenseArrayTransform layer) {
		super(0, input[0].size-1, 0, output[0].size-1);
		this.input = input;
		this.output = output;
		this.layer = layer;
		validate();
	}

	@Override
	public void forward() {
		for(DenseNeuronArray i : input){
			if(!i.isOutputInitialized()){
				throw new RuntimeException("input is null");
			}
		}
		for(DenseNeuronArray o : output){
			if(!o.isOutputInitialized()){
				throw new RuntimeException("output is null");
			}
		}
		long time = System.currentTimeMillis();
		layer.forward(input, inputStart, inputEnd, output, outputStart, outputEnd, this);
		((Layer)layer).addForward(System.currentTimeMillis()-time);
	}

	@Override
	public void backward() {
		long time = System.currentTimeMillis();
		layer.backward(input, inputStart, inputEnd, output, outputStart, outputEnd, this);
		((Layer)layer).addBackward(System.currentTimeMillis()-time);

		
	}	

	@Override
	public Layer getLayer() {		
		return (Layer)layer;
	}

	@Override
	public String toString() {
		String ret = "from: array:\n";
		for(int i = 0; i < input.length; i++){
			ret+="["+i+"] " + input[i]+"\n";
		}
		ret += "\n";
		ret += "to: array:\n";
		for(int i = 0; i < output.length; i++){
			ret+="["+i+"] " + output[i]+"\n";
		}
		ret += "indexes : " + inputStart + ":" + inputEnd + " -> " + outputStart + ":"+outputEnd; 
		return ret;
	}

	public DenseNeuronArray getInput() {
		return input[0];
	}

	public DenseNeuronArray getOutput() {
		return output[0];
	}
	
	@Override
	public NeuronArray[] getInputArray() {
		return input;
	}
	
	@Override
	public NeuronArray[] getOutputArray() {
		return output;
	}
}
