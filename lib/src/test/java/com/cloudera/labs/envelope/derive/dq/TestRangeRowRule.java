/*
 * Copyright (c) 2015-2019, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

package com.cloudera.labs.envelope.derive.dq;

import static com.cloudera.labs.envelope.validate.ValidationAssert.assertNoValidationFailures;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.cloudera.labs.envelope.spark.RowWithSchema;
import com.cloudera.labs.envelope.utils.ConfigUtils;
import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;

public class TestRangeRowRule {

  @Test
  public void testAgeRangeInt() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.IntegerType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("age"));
    configMap.put(RangeRowRule.FIELD_TYPE_CONFIG, "int");
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList(0,105));
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", 34, new BigDecimal("0.00"));
    assertTrue("Row should pass rule", rule.check(row1));

    Row row2 = new RowWithSchema(schema, "Webster1", "Websta1", 110, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row2));

    Row row3 = new RowWithSchema(schema, "", "Ian1", 106, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row3));

    Row row4 = new RowWithSchema(schema, "First Last", "Ian Last", 105, new BigDecimal("450.10"));
    assertTrue("Row should pass rule", rule.check(row4));
  }

  @Test
  public void testAgeRangeLong() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.LongType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("age"));
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList(0l,105l));
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", 34l, new BigDecimal("0.00"));
    assertTrue("Row should pass rule", rule.check(row1));

    Row row2 = new RowWithSchema(schema, "Webster1", "Websta1", 110l, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row2));

    Row row3 = new RowWithSchema(schema, "", "Ian1", 110l, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row3));

    Row row4 = new RowWithSchema(schema, "First Last", "Ian Last", 100l, new BigDecimal("450.10"));
    assertTrue("Row should pass rule", rule.check(row4));
  }

  @Test
  public void testAgeRangeFloat() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.FloatType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("age"));
    configMap.put(RangeRowRule.FIELD_TYPE_CONFIG, "float");
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList(0.1,105.0));
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", 34.0f, new BigDecimal("0.00"));
    assertTrue("Row should pass rule", rule.check(row1));

    Row row2 = new RowWithSchema(schema, "Webster1", "Websta1", 110.0f, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row2));

    Row row3 = new RowWithSchema(schema, "", "Ian1", 110.0f, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row3));

    Row row4 = new RowWithSchema(schema, "First Last", "Ian Last", 100.0f, new BigDecimal("450.10"));
    assertTrue("Row should pass rule", rule.check(row4));
  }

  @Test
  public void testAgeRangeDouble() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.DoubleType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("age"));
    configMap.put(RangeRowRule.FIELD_TYPE_CONFIG, "float");
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList(0.1,105.0));
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", new Float(34.0), new BigDecimal("0.00"));
    assertTrue("Row should pass rule", rule.check(row1));

    Row row2 = new RowWithSchema(schema, "Webster1", "Websta1", new Float(110.0), new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row2));

    Row row3 = new RowWithSchema(schema, "", "Ian1", new Float(110.0), new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row3));

    Row row4 = new RowWithSchema(schema, "First Last", "Ian Last", new Float(100.0), new BigDecimal("450.10"));
    assertTrue("Row should pass rule", rule.check(row4));
  }

  @Test
  public void testAgeRangeDecimal() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.DoubleType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("candycrushscore"));
    configMap.put(RangeRowRule.FIELD_TYPE_CONFIG, "decimal");
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList("-1.56","400.45"));
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", 34.0, new BigDecimal("-1.00"));
    assertTrue("Row should pass rule", rule.check(row1));

    Row row2 = new RowWithSchema(schema, "Webster1", "Websta1", 110.0, new BigDecimal("-1.57"));
    assertFalse("Row should not pass rule", rule.check(row2));

    Row row3 = new RowWithSchema(schema, "", "Ian1", 110.0, new BigDecimal("450.10"));
    assertFalse("Row should not pass rule", rule.check(row3));

    Row row4 = new RowWithSchema(schema, "First Last", "Ian Last", 100.0, new BigDecimal("400.45"));
    assertTrue("Row should pass rule", rule.check(row4));
  }
  
  public void testDontIgnoreNulls() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.IntegerType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("age"));
    configMap.put(RangeRowRule.FIELD_TYPE_CONFIG, "int");
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList(0,105));
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", null, new BigDecimal("0.00"));
    assertFalse("Row should not pass rule", rule.check(row1));
  }
  
  @Test
  public void testIgnoreNulls() {
    StructType schema = new StructType(new StructField[] {
        new StructField("name", DataTypes.StringType, false, Metadata.empty()),
        new StructField("nickname", DataTypes.StringType, false, Metadata.empty()),
        new StructField("age", DataTypes.IntegerType, false, Metadata.empty()),
        new StructField("candycrushscore", DataTypes.createDecimalType(), false, Metadata.empty())
    });

    Map<String, Object> configMap = new HashMap<>();
    configMap.put(RangeRowRule.FIELDS_CONFIG, Lists.newArrayList("age"));
    configMap.put(RangeRowRule.FIELD_TYPE_CONFIG, "int");
    configMap.put(RangeRowRule.RANGE_CONFIG, Lists.newArrayList(0,105));
    configMap.put(RangeRowRule.IGNORE_NULLS_CONFIG, true);
    Config config = ConfigFactory.parseMap(configMap);

    RangeRowRule rule = new RangeRowRule();
    assertNoValidationFailures(rule, config);
    rule.configure(config);
    rule.configureName("agerange");

    Row row1 = new RowWithSchema(schema, "Ian", "Ian", null, new BigDecimal("0.00"));
    assertTrue("Row should pass rule", rule.check(row1));
  }
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void testRangeDataTypes() throws Exception {
    Config config = ConfigUtils.configFromResource("/dq/dq-range-rules.conf").getConfig("steps");
    StructType schema = new StructType(new StructField[] {
      new StructField("fa", DataTypes.LongType, false, Metadata.empty()),
      new StructField("fi", DataTypes.IntegerType, false, Metadata.empty()),
      new StructField("fl", DataTypes.LongType, false, Metadata.empty()),
      new StructField("ff", DataTypes.FloatType, false, Metadata.empty()),
      new StructField("fe", DataTypes.DoubleType, false, Metadata.empty()),
      new StructField("fd", DataTypes.createDecimalType(), false, Metadata.empty())
    });
    Row row = new RowWithSchema(schema, new Long(2), 2, new Long(2), new Float(2.0), 2.0, new BigDecimal("2.0"));
      
    ConfigObject rro =  config.getObject("dq1.deriver.rules") ;
    for ( String rulename : rro.keySet() ) {
      Config rrc = rro.toConfig().getConfig(rulename);
      RangeRowRule rrr = new RangeRowRule() ;
      rrr.configure(rrc);
      rrr.configureName(rulename);
      assertTrue("Row should pass rule " + rulename, rrr.check(row));
    }
  }
}
