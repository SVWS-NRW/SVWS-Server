<script setup lang="ts">
	import { hstEvent } from 'histoire/client';

	import SvwsUiRadioGroup from "./RadioGroup.vue";
	import SvwsUiRadioOption from "./RadioOption.vue";

	function onInput(event: Event) {
		hstEvent('input', event);
	}
</script>

<template>
	<Story title="SVWS UI/Controls/Radio Group">
		<svws-ui-radio-group>
			<svws-ui-radio-option value="option1" name="radioInputExample" label="Option 1" @input="onInput" />
			<svws-ui-radio-option value="option2" name="radioInputExample" label="Option 2" disabled @input="onInput" />
			<svws-ui-radio-option value="option3" name="radioInputExample" label="Option 3" @input="onInput" />
		</svws-ui-radio-group>
	</Story>
</template>