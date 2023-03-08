<script setup lang="ts">
	import { ref } from 'vue';

	const selected = ref("option1");
</script>

<template>
	<Story title="SVWS UI/Controls/Select Input">
		<Variant title="Default">
			<svws-ui-select-input placeholder="Select Input Placeholder" :valid-input="true" :options="[
				{
					index: 'option1', label: 'Option 1'
				}, { index: 'option2', label: 'Option 2' }, {
					index: 'option3', label: 'Option 3'
				}]" />
		</Variant>
		<Variant title="Selected">
			<svws-ui-select-input v-model="selected" placeholder="Select Input Placeholder" :valid-input="true" :options="[
				{
					index: 'option1', label: 'Option 1'
				}, { index: 'option2', label: 'Option 2' }, {
					index: 'option3', label: 'Option 3'
				}]" />
		</Variant>
	</Story>
</template>