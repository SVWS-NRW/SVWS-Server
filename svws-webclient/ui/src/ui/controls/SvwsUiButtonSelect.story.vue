<template>
	<Story title="Button mit Auswahl" id="svws-ui-button-select" icon="ri:cursor-line" :layout="{ type: 'single', iframe: false }" auto-props-disabled>
		<Variant title="Default">
			<svws-ui-input-wrapper class="p-2">
				<svws-ui-button-select @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions" />
				<svws-ui-button-select type="danger" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions" />
				<svws-ui-button-select type="secondary" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions" />
				<svws-ui-button-select type="secondary" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions">
					<template #icon><span class="icon i-ri-printer-line" /></template>
				</svws-ui-button-select>
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper class="p-2 page--statistik mt-10">
				<svws-ui-button-select @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions" />
				<svws-ui-button-select type="danger" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions" />
				<svws-ui-button-select type="secondary" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions" />
				<svws-ui-button-select type="secondary" @click="() => console.log('Default Button clicked')" :dropdown-actions="dropdownActions">
					<template #icon><span class="icon i-ri-printer-line" /></template>
				</svws-ui-button-select>
			</svws-ui-input-wrapper>
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
		{ key: 4, text: "", separator: true, action: () => {} },
		{ key: 5, text: "Button 4", action: () => console.log("Button 4 clicked not possible") },
	];
</script>
