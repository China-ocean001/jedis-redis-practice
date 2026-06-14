package cn.jee.redis;

import cn.jee.redis.domain.Stu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import cn.jee.redis.util.CompSign;
public class StuMisTest {
  private StuMis stuMis;
  private Random random;
  private static final int TEST_DATA_COUNT = 15;

  // 用于记录所有添加的学生信息，便于验证方法返回值是否正确
  private List<Stu> allStudents;

  @BeforeEach
  public void setUp(TestInfo testInfo) {
    stuMis = new StuMis();
    stuMis.clear();
    if (testInfo.getTags().contains("beforeAdd")) {
      return;
    }
    random = new Random();
    allStudents = new ArrayList<>();

    // 添加测试数据
    for (int i = 0; i < TEST_DATA_COUNT; i++) {
      Stu stu = new Stu();
      stu.setUsername("Student" + i);
      stu.setJava(format(random.nextDouble()));
      stu.setMath(format(random.nextDouble()));
      stuMis.add(stu);
      System.out.println(stu);

      // 记录添加的学生信息
      allStudents.add(stu);
    }
  }

  private double format(double value) {
    return Math.round(value * 1000) / 10.0;
  }

  @Test
  @Tag("beforeAdd")
  public void testNewId() {
    System.out.println("newId");
    long id1 = stuMis.newId();
    assertEquals(1, id1, "初始ID应该为1");
    long id2 = stuMis.newId();
    assertEquals(2, id2, "应该为2");
    assertTrue(id2 > id1, "新生成的ID应该大于之前生成的ID");
  }

  @Test
  public void testAdd() {
    // 添加一个新学生
    Stu stu = allStudents.get(TEST_DATA_COUNT / 3);

    // 验证是否成功添加
    Stu found = stuMis.findById(stu.getId());
    assertNotNull(found, "应该能找到添加的学生");
    assertEquals(stu.getUsername(), found.getUsername(), "用户名应该匹配");
    assertEquals(stu.getJava(), found.getJava(), 0.001, "Java成绩应该匹配");
    assertEquals(stu.getMath(), found.getMath(), 0.001, "数学成绩应该匹配");
  }

  @Test
  public void testTop5ByMath() {
    // 使用已有的学生数据进行测试
    String[] topNames = stuMis.top5ByMath();
    System.out.println(Arrays.toString(topNames));
    assertNotNull(topNames, "返回的数组不应为null");
    assertEquals(5, topNames.length, "返回的数组长度应该为5");

    // 验证返回的学生姓名是否与预期一致
    // 按数学成绩从高到低排序
    List<Stu> sortedByMath = new ArrayList<>(allStudents);
    // sortedByMath.sort(Comparator.comparingDouble(Stu::getMath).reversed());
    String[] expectedNames = sortedByMath.stream().sorted(Comparator.comparingDouble(Stu::getMath).reversed()).limit(5)
        .map(Stu::getUsername).toArray(String[]::new);
    System.out.println(Arrays.toString(expectedNames));
    assertArrayEquals(expectedNames, topNames, "返回的学生姓名应与预期一致");
  }

  @Test
  public void testBetweenJava() {
    double min = 60.0;
    double max = 80.0;

    // 使用已有的学生数据进行测试
    double[] scores = stuMis.betweenJava(min, max);
    assertNotNull(scores, "返回的数组不应为null");

    // 验证所有成绩都在范围内
    for (double score : scores) {
      assertTrue(score >= min && score <= max, "成绩应该在指定范围内");
    }

    // 从allStudents中筛选出Java成绩在指定范围内的学生
    double[] expectedScores = allStudents.stream()
        .filter(stu -> stu.getJava() >= min && stu.getJava() <= max)
        .map(Stu::getJava)
        .mapToDouble(Double::doubleValue)
        .toArray();

    // 验证返回的成绩数量是否与预期一致
    assertEquals(expectedScores.length, scores.length, "返回的成绩数量应该与预期一致");

  }

  @Test
  public void testFindById() {
    // 使用已有的学生数据进行测试
    if (allStudents.isEmpty()) {
      fail("测试数据为空，无法进行测试");
      return;
    }

    // 选择第一个学生进行测试
    Stu stu = allStudents.get(0);
    long id = stu.getId();

    // 查找并验证
    Stu found = stuMis.findById(id);
    assertNotNull(found, "应该能找到添加的学生");
    assertEquals(stu.getUsername(), found.getUsername(), "用户名应该匹配");
    assertEquals(stu.getJava(), found.getJava(), 0.001, "Java成绩应该匹配");
    assertEquals(stu.getMath(), found.getMath(), 0.001, "数学成绩应该匹配");

    // 测试查找不存在的ID
    Stu notFound = stuMis.findById(999999);
    assertNull(notFound, "不存在的ID应该返回null");
  }

  @Test
  public void testPageByJava() {
    // 使用已有的学生数据进行测试

    // 按Java成绩从低到高排序，用于验证分页结果
    List<Stu> sortedStudents = new ArrayList<>(allStudents);
    sortedStudents.sort(Comparator.comparingDouble(Stu::getJava));

    int pageSize = 5;
    int totalPages = (int) Math.ceil((double) sortedStudents.size() / pageSize);

    // 测试第一页
    List<Stu> page1 = stuMis.pageByJava(1, pageSize);
    assertNotNull(page1, "返回的列表不应为null");
    assertTrue(page1.size() > 0, "第一页应该有数据");
    assertTrue(page1.size() <= pageSize, "第一页数据不应超过每页大小");

    // 验证排序是否正确（Java成绩从低到高）
    for (int i = 0; i < page1.size() - 1; i++) {
      assertTrue(page1.get(i).getJava() <= page1.get(i + 1).getJava(),
          "Java成绩应该是从低到高排序的");
    }

    // 如果有足够的数据，测试第二页
    if (sortedStudents.size() > pageSize) {
      List<Stu> page2 = stuMis.pageByJava(2, pageSize);
      assertNotNull(page2, "返回的列表不应为null");
      assertTrue(page2.size() > 0, "第二页应该有数据");

      // 验证第二页的成绩都高于第一页的最高成绩
      if (!page1.isEmpty() && !page2.isEmpty()) {
        double page1MaxScore = page1.get(page1.size() - 1).getJava();
        assertTrue(page2.get(0).getJava() >= page1MaxScore,
            "第二页的最低成绩应该大于等于第一页的最高成绩");
      }

      // 验证排序是否正确
      for (int i = 0; i < page2.size() - 1; i++) {
        assertTrue(page2.get(i).getJava() <= page2.get(i + 1).getJava(),
            "Java成绩应该是从低到高排序的");
      }   
    }
  }
  String[] getNames(){
    String[] names=new String[4];
    for(int i=0;i<3;i++){      
      names[i]=""+Math.round(Math.random()*100000);
    }
    names[3]=LocalDateTime.now().hashCode()+"";
    Arrays.sort(names);
    return names;
  }
 
  @TestFactory
  Stream<DynamicTest> dynamicTestsFromStream() {
    return Stream.of(getNames())
        .map(str -> DynamicTest.dynamicTest("测试" + str, () -> {
          
        }));
  }
 
  @Test
  public void testSign(){
    try {
      assertTrue(CompSign.doVerify(), "测试文件被修改过");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}