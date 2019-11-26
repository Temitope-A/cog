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

package org.apache.flink.table.plan.nodes.dataset

import java.util

import org.apache.calcite.plan.{RelOptCluster, RelOptTable, RelTraitSet}
import org.apache.calcite.rel.RelNode
import org.apache.calcite.rel.`type`.RelDataType
import org.apache.calcite.rel.core.TableScan
import org.apache.calcite.rel.metadata.RelMetadataQuery
import org.apache.calcite.schema.TransientTable
import org.apache.calcite.schema.impl.ListTransientTable
import org.apache.flink.api.java.DataSet
import org.apache.flink.table.api.BatchQueryConfig
import org.apache.flink.table.api.internal.BatchTableEnvImpl
import org.apache.flink.table.plan.schema.RowSchema
import org.apache.flink.types.Row

class DataSetTransientTableScan(cluster: RelOptCluster,
                                traitSet: RelTraitSet,
                                table: RelOptTable,
                                tableSource: TransientTable,
                                selectedFields: Option[Array[String]],
                                rowType: RelDataType,
                                val tableSource1: ListTransientTable
                               )
  extends TableScan(cluster, traitSet, table)
    with BatchScan {

  override def deriveRowType(): RelDataType = table.getRowType

  override def estimateRowCount(mq: RelMetadataQuery): Double = 1000L

  override def copy(traitSet: RelTraitSet, inputs: util.List[RelNode]): RelNode = {
    new DataSetTransientTableScan(cluster, traitSet, table, tableSource, selectedFields, rowType, tableSource1)
  }

  override def translateToPlan(tableEnv: BatchTableEnvImpl, queryConfig: BatchQueryConfig): DataSet[Row] = {
    println(">>>>>>>>>>>>>>>>>>>>>>>++++++++++++++++++++++ INSIDE TRANSIENTTABLESCAN...")

    val schema = new RowSchema(deriveRowType)
    val config = tableEnv.getConfig
    convertToInternalRow(schema, tableSource1.asInstanceOf[DataSet[Any]], List(1, 2).toArray, config, Option.empty)
  }
}
