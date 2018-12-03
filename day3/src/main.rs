extern crate regex;

use std::io;
use std::io::prelude::*;
use regex::Regex;
use std::collections::HashMap;

struct Claim {
    id: i32,
    top: usize,
    left: usize,
    width: usize,
    height: usize,
    unique_count: i32,
}

const MATRIX_SIZE: usize = 1_000;

fn main() {
    let mut claims = HashMap::new();
    let mut matrix = vec![vec![0; MATRIX_SIZE]; MATRIX_SIZE];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        let claim = create_claim(&s);
        claims.insert(claim.id, claim);
    }

    populate_matrix(&claims, &mut matrix);
    println!("Part 1: {}", solve_part1(&matrix));
    println!("Part 2: {}", solve_part2(&mut claims, &matrix));
}

fn solve_part1(matrix: &Vec<Vec<i32>>) -> i32 {
    let mut count = 0;

    for i in 0..MATRIX_SIZE {
        for j in 0..MATRIX_SIZE {
            if matrix[i][j] == -1 {
                count += 1;
            }
        }
    }

    count
}

fn solve_part2(claims: &mut HashMap<i32, Claim>, matrix: &Vec<Vec<i32>>) -> i32 {
    for i in 0..MATRIX_SIZE {
        for j in 0..MATRIX_SIZE {
            if matrix[i][j] > 0 {
                let claim = match claims.get_mut(&matrix[i][j]) {
                    Some(claim) => claim,
                    None => panic!("no claim found"),
                };
                claim.unique_count += 1;
            }
        }
    }

    for (_id, claim) in claims {
        if claim.width * claim.height == (claim.unique_count as usize) {
            return claim.id;
        }
    }

    0
}

fn populate_matrix(claims: &HashMap<i32, Claim>, matrix: &mut Vec<Vec<i32>>) {
    for (_id, claim) in claims {
        for i in claim.top..(claim.top + claim.height) {
            for j in claim.left..(claim.left + claim.width) {
                if matrix[i][j] == 0 {
                    matrix[i][j] = claim.id;
                } else {
                    matrix[i][j] = -1;
                }
            }
        }
    }

}

fn create_claim(string: &str) -> Claim {
    let re = Regex::new(r"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)").unwrap();
    let mut claim = Claim { id: 0, left: 0, top: 0, width: 0, height: 0,
                            unique_count: 0 };

    for cap in re.captures_iter(string) {
        claim.id = parse_int(&cap[1]);
        claim.left = parse_usize(&cap[2]);
        claim.top = parse_usize(&cap[3]);
        claim.width = parse_usize(&cap[4]);
        claim.height = parse_usize(&cap[5]);
    }

    claim
}

fn parse_int(string: &str) -> i32 {
    match string.parse() {
        Ok(num) => num,
        Err(_) => 0,
    }
}

fn parse_usize(string: &str) -> usize {
    match string.parse() {
        Ok(num) => num,
        Err(_) => 0,
    }
}
