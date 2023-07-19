<script setup lang="ts">
	import { logEvent } from 'histoire/client';
	import { ref } from 'vue';

	const modelValue = ref("This is a filled textarea component");

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>

<template>
	<Story title="Textarea Input" icon="ri:align-left" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Empty">
			<div class="py-4">
				<svws-ui-textarea-input model-value="" valid placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Filled">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" valid placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Invalid">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" :valid="false" placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Statistics">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" valid statistics placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Disabled">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" valid disabled placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Non-resizable">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" valid placeholder="Textarea Input Placeholder" resizeable="none" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Autoresize">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" resizeable="none" autoresize @input="onInput" />
			</div>
		</Variant>
	</Story>
</template>
