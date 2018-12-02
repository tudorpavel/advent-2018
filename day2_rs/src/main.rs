use std::io;
use std::io::prelude::*;

fn main() {
    let mut ids = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        ids.push(s);
    }

    println!("Part 1: {}", solve_part1(&ids));
    println!("Part 2: {}", solve_part2(&ids));
}

fn solve_part1(ids: &Vec<String>) -> i32 {
    let mut sum1 = 0;
    let mut sum2 = 0;
    for id in ids.iter() {
        if has_char_count(&id, 2) {
            sum1 += 1;
        }
        if has_char_count(&id, 3) {
            sum2 += 1;
        }
    }

    sum1 * sum2
}

fn solve_part2(ids: &Vec<String>) -> String {
    let mut result = String::from("");

    for i in 0..(ids.len() - 1) {
        for j in (i + 1)..ids.len() {
            result = intersect(&ids[i], &ids[j]);
            if ids[i].len() - result.len() == 1 {
                return result;
            }
        }
    }

    result
}

fn has_char_count(string: &String, count: usize) -> bool {
    for character in string.chars() {
        if count == string.matches(character).count() {
            return true;
        }
    }

    false
}

fn intersect(string1: &String, string2: &String) -> String {
    assert_eq!(string1.len(), string2.len());

    let mut result = String::from("");

    for i in 0..string1.len() {
        if char_at(&string1, i) == char_at(&string2, i) {
            result.push(char_at(&string1, i));
        }
    }

    result
}

fn char_at(string: &String, pos: usize) -> char {
    string[pos..].chars().next().unwrap()
}
