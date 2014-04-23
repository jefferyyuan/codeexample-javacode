package org.codeexample.guava;

import static junit.framework.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;

public class UsingSplitterJoiner {

  private static final Splitter COMMA_SPLITTER = Splitter.on(',');

  @Test
  public void testMapSplitter() {
    Map<String, String> m = COMMA_SPLITTER.withKeyValueSeparator(
        Splitter.on(':').trimResults()).split(
        "boy  : tom , girl: tina , cat  :, dog: tommy ");

    ImmutableMap<String, String> expected = ImmutableMap.of("boy", "tom",
        "girl", "tina", "cat", "kitty", "dog", "tommy");
    System.out.println(Objects.toStringHelper(m).addValue(m).toString());
  }

  @Test
  public void testEntries() {
    MapJoiner j = Joiner.on(";").withKeyValueSeparator(":");
    String str = j.join(ImmutableMultimap.of("1", "a", "1", "b").entries());
    System.out.println(str);
    assertEquals("1:a;1:b", str);
  }
}
