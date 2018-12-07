extern crate regex;

use std::io;
use std::io::prelude::*;
use regex::Regex;
use std::collections::BTreeMap;

fn main() {
    let mut lines = vec![];

    let stdin = io::stdin();
    for line in stdin.lock().lines() {
        let s = line.unwrap();
        lines.push(s);
    }

    let graph = make_graph(lines);

    println!("Part 1: {}", solve_part1(&graph));
    // println!("Part 2: {}", solve_part2(&graph));
}

fn solve_part1(graph: &BTreeMap<String, Vec<String>>) -> String {
    let mut graph = graph.clone();
    let mut result = String::from("");

    while graph.len() > 0 {
        let mut candidate_id = String::from("");

        for (id, parents) in &graph {
            let mut is_candidate = true;

            for parent in parents {
                if graph.contains_key(parent) {
                    is_candidate = false;
                    break;
                }
            }

            if is_candidate {
                result.push_str(id.as_str());
                candidate_id = id.clone();
                break;
            }
        }

        graph.remove(&candidate_id);
    }

    result
}

fn make_graph(lines: Vec<String>) -> BTreeMap<String, Vec<String>> {
    let mut graph = BTreeMap::new();
    let re = Regex::new(r"Step (\w) must be finished before step (\w) can begin.").unwrap();

    for line in lines {
        for cap in re.captures_iter(line.as_str()) {
            let parent_id = String::from(&cap[1]);
            let child_id = String::from(&cap[2]);

            graph.entry(parent_id.clone()).or_insert(vec![]);
            graph.entry(child_id.clone()).or_insert(vec![]);

            let child = graph.get_mut(&child_id).unwrap();
            &child.push(parent_id.clone());
        }
    }

    graph
}
