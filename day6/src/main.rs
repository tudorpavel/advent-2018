extern crate regex;

use std::io;
use std::io::prelude::*;
use regex::Regex;

struct Point {
    x: i32,
    y: i32,
    infinite: bool,
    count: i32,
}

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    let mut points = lines.iter().map(|l| make_point(l)).collect();

    println!("Part 1: {}", solve_part1(&mut points));
    // println!("Part 2: {}", solve_part2(&points));
}

fn solve_part1(points: &mut Vec<Point>) -> i32 {
    let mut min_x = 1_000;
    let mut min_y = 1_000;
    let mut max_x = 0;
    let mut max_y = 0;

    for point in points.iter() {
        if point.x < min_x { min_x = point.x }
        if point.y < min_y { min_y = point.y }
        if point.x > max_x { max_x = point.x }
        if point.y > max_y { max_y = point.y }
    }

    let mut null_point = Point { x: -1, y: -1, infinite: false, count: 0 };

    for y in min_y..=max_y {
        for x in min_x..=max_x {
            let mut min_distance = 1_000;
            let mut closest_point = &mut null_point;

            for point in points.iter_mut() {
                let distance = distance(&x, &y, &point.x, &point.y);

                if distance < min_distance {
                    min_distance = distance;
                    closest_point = point;
                } else if distance == min_distance && closest_point.x > -1 {
                    closest_point = &mut null_point;
                }
            }

            if closest_point.x > -1 {
                if x == min_x || x == max_x || y == min_y || y == max_y {
                    closest_point.infinite = true;
                }

                if !closest_point.infinite {
                    closest_point.count += 1;
                }
            }

            print!("({}, {}, {}, {}) ", x, y, closest_point.x, closest_point.y);
        }

        println!();
    }

    0
}

fn make_point(string: &str) -> Point {
    let re = Regex::new(r"(\d+), (\d+)").unwrap();
    let mut point = Point { x: -1, y: -1, infinite: false, count: 0 };

    for cap in re.captures_iter(string) {
        point.x = parse_int(&cap[1]);
        point.y = parse_int(&cap[2]);
    }

    point
}

fn parse_int(string: &str) -> i32 {
    match string.parse() {
        Ok(num) => num,
        Err(_) => 0,
    }
}

fn distance(x1: &i32, y1: &i32, x2: &i32, y2: &i32) -> i32 {
    (x1 - x2).abs() + (y1 - y2).abs()
}
