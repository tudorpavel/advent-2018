# frozen_string_literal: true

def intersect(str1, str2)
  raise 'str1 and str2 have different lengths' if str1.length != str2.length

  intersection = +''

  0.upto(str1.length - 1) do |i|
    intersection << str1[i] if str1[i] == str2[i]
  end

  intersection
end

def solve(ids)
  ids.each_with_index do |id, i|
    (i + 1).upto(ids.size - 1) do |j|
      result = intersect(id, ids[j])
      return result if id.size - result.size == 1
    end
  end
end

ids = ARGF.readlines

puts solve(ids)
