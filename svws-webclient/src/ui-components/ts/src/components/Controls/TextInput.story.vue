<script setup lang="ts">
	import { hstEvent } from 'histoire/client';
	import SvwsUiTextInput from "./TextInput.vue";

	function onInput(event: Event) {
		hstEvent('input', event);
	}
</script>

<template>
	<Story title="SVWS UI/Controls/Text Input">
		<Variant title="Empty">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid @input="onInput" />
		</Variant>

		<Variant title="Filled">
			<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" valid @input="onInput" />
		</Variant>

		<Variant title="Invalid">
			<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" :valid="false" @input="onInput" />
		</Variant>

		<Variant title="Disabled">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid disabled @input="onInput" />
		</Variant>

		<Variant title="Statistics">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid statistics @input="onInput" />
		</Variant>

		<Variant title="Prefix">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid url @input="onInput" />
		</Variant>
	</Story>
</template>