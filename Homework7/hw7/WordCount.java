/**
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *   * or more contributor license agreements.  See the NOTICE file
 *    * distributed with this work for additional information
 *     * regarding copyright ownership.  The ASF licenses this file
 *      * to you under the Apache License, Version 2.0 (the
 *       * "License"); you may not use this file except in compliance
 *        * with the License.  You may obtain a copy of the License at
 *         *
 *          *     http://www.apache.org/licenses/LICENSE-2.0
 *           *
 *            * Unless required by applicable law or agreed to in writing, software
 *             * distributed under the License is distributed on an "AS IS" BASIS,
 *              * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *               * See the License for the specific language governing permissions and
 *                * limitations under the License.
 *                 */
//package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.GenericOptionsParser;


public class WordCount 
{
	public static String keyString;
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
	{
    
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		private WordCount wc = new WordCount();
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		{
			//Path filePath = ((FileSplit) context.getInputSplit()).getPath();
			String filePathString = ((FileSplit) context.getInputSplit()).getPath().toString();
			StringTokenizer itr = new StringTokenizer(value.toString());
			Configuration conf = context.getConfiguration();
			while (itr.hasMoreTokens()) 
			{
				String current = itr.nextToken();
				if(current.compareTo(conf.get("keyString")) == 0)
				{
					word.set( current + " " + filePathString+"/ ");
					context.write(word, one);
				}//end of if
			}//end of while
		}//end of map
	}//end of TokenizerMapper
  
	public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> 
	{
		private IntWritable result = new IntWritable();
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
		{
			int sum = 0;
			for (IntWritable val : values) 
			{
				sum += val.get();
			}//end of for loop
			result.set(sum);
			context.write(key, result);
		}//end of reduce
	}//end of IntSumReducer	

	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length < 3) 
		{
			System.err.println("Usage: wordcount <in> [<in>...] <out> Key");
			System.exit(2);
		}//end of if statement
		conf.set("keyString", otherArgs[otherArgs.length - 1]);
		//System.out.println(wc.keyString);
		Job job = new Job(conf, "word count");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		for (int i = 0; i < otherArgs.length - 2; ++i) 
		{
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}//end of for loop
		FileOutputFormat.setOutputPath(job,
		new Path(otherArgs[otherArgs.length - 2]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}//end of main
}//end of WordCount
