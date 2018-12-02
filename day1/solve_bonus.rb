frequency = 0;
frequencies = {};
repeat_frequency_found = false;

frequency_changes = ARGF.readlines.map(&:to_i)

while !repeat_frequency_found
  frequency_changes.each do |frequency_change|
    frequency += frequency_change

    if frequencies[frequency]
      repeat_frequency_found = true
      break
    end

    frequencies[frequency] = true
  end
end

puts frequency
