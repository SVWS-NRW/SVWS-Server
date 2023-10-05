<template>
	<Story title="Button mit Auswahl" id="svws-ui-button-select" icon="ri:cursor-line" auto-props-disabled>
		<Variant title="Default">
			<div class="p-3 flex flex-wrap gap-3">
				<svws-ui-button-select @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions">
					Default
				</svws-ui-button-select>
				<div class="w-full" />
				<svws-ui-button-select type="secondary" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions">
					Default Secondary
				</svws-ui-button-select>
			</div>
			<template #source>
				{{ `
<svws-ui-button-select
  type="..."
  @click="() => console.log('Klick Default')"
  :dropdown-actions="[
    { key: 1, text: 'Label 1', action: () => console.log('Klick 1') },
    { key: 2, text: 'Label 2', action: () => console.log('Klick 2') }
  ]">
  Default Button Text
</svws-ui-button-select>
        ` }}
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	const dropdownActions = [
		{ key: 1, text: "Button 1", action: () => console.log("Button 1 clicked") },
		{ key: 2, text: "Button 2", action: () => console.log("Button 2 clicked") },
		{ key: 3, text: "Button 3", action: () => console.log("Button 3 clicked") },
	];
</script>
