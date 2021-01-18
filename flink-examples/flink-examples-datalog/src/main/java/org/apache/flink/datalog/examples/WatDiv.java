/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.flink.datalog.examples;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.datalog.BatchDatalogEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.types.IntValue;
import java.io.File;

public class WatDiv {
	public static void main(String[] args) throws Exception {
		String testFolderPath = "s3://wolf4495/watdiv/";
		String inputProgram =
			"madeBy(X,Y) :- author(X,Y) .\n" +
				"madeBy(X,Y) :- editor(X,Y) .\n" +
				"madeBy(X,Y) :- director(X,Y) .\n" +

				"features(X,Y) :- actor(X,Y) .\n" +
				"features(X,Y) :- artist(X,Y) .\n" +

				"directed(Z,Y) :- features(X,Y), madeBy(X,Z) .";
		String query1 = "madeBy(X,Y)?";
		String query2 = "features(X,Y)?";
		String query3 = "directed(X,Y)?";

		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		EnvironmentSettings settings = EnvironmentSettings
			.newInstance()
			.useDatalogPlanner()
			.inBatchMode()
			.build();
		BatchDatalogEnvironment datalogEnv = BatchDatalogEnvironment.create(env, settings);

		String[] relationNames = {"editor", "director", "actor", "artist"};

		DataSet<Tuple2<IntValue, IntValue>> author = env.readCsvFile(testFolderPath + "author.csv").fieldDelimiter(",").types(IntValue.class, IntValue.class);
		datalogEnv.registerDataSet("author", author, "v1,v2");

		for (String relation: relationNames){
			String filePath = testFolderPath + relation + ".csv";
			DataSet<Tuple2<IntValue, IntValue>> dataset = env.readCsvFile(filePath).fieldDelimiter(",").types(IntValue.class, IntValue.class);
			datalogEnv.registerDataSet(relation, dataset, "v1,v2");
		}

		//DataSet<Tuple2<IntValue, IntValue>> author = env.readCsvFile(testFolderPath + "author.csv").fieldDelimiter(",").types(IntValue.class, IntValue.class);
		//datalogEnv.registerDataSet("author", author, "v1,v2");
		//DataSet<Tuple2<IntValue, IntValue>> editor = env.readCsvFile(testFolderPath + "editor.csv").fieldDelimiter(",").types(IntValue.class, IntValue.class);
		//datalogEnv.registerDataSet("editor", editor, "v1,v2");
		//DataSet<Tuple2<IntValue, IntValue>> director = env.readCsvFile(testFolderPath + "director.csv").fieldDelimiter(",").types(IntValue.class, IntValue.class);
		//datalogEnv.registerDataSet("director", director, "v1,v2");
		//DataSet<Tuple2<IntValue, IntValue>> artist = env.readCsvFile(testFolderPath + "artist.csv").fieldDelimiter(",").types(IntValue.class, IntValue.class);
		//datalogEnv.registerDataSet("artist", artist, "v1,v2");
		//DataSet<Tuple2<IntValue, IntValue>> actor = env.readCsvFile(testFolderPath + "actor.csv").fieldDelimiter(",").types(IntValue.class, IntValue.class);
		//datalogEnv.registerDataSet("actor", actor, "v1,v2");

		Table queryResult1 = datalogEnv.datalogQuery(inputProgram, query1);
		DataSet<Tuple2<IntValue, IntValue>> resultDS1 = datalogEnv.toDataSet(queryResult1, author.getType());
		System.out.println(resultDS1.count());
		Table queryResult2 = datalogEnv.datalogQuery(inputProgram, query2);
		DataSet<Tuple2<IntValue, IntValue>> resultDS2 = datalogEnv.toDataSet(queryResult2, author.getType());
		System.out.println(resultDS2.count());
		Table queryResult3 = datalogEnv.datalogQuery(inputProgram, query2);
		DataSet<Tuple2<IntValue, IntValue>> resultDS3 = datalogEnv.toDataSet(queryResult3, author.getType());
		System.out.println(resultDS3.count());

	}
}
