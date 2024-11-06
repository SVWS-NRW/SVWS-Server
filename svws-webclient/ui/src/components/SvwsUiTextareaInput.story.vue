<template>
	<Story title="Textarea Input" id="svws-ui-textarea-input" icon="ri:align-left" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input model-value="" placeholder="Textarea Input Placeholder" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Resize props" id="Resize">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: vertical (default)" resizeable="vertical" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: horizontal" resizeable="horizontal" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: both" resizeable="both" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: none" resizeable="none" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: none + autoresize" resizeable="none" autoresize @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Invalid" id="Invalid">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input v-model="modelValue" :valid="v=>false" placeholder="Invalid Textarea" resizeable="vertical" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="Required Length Textarea" resizeable="vertical" @input="onInput" :max-len="10" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="p-4">
				<svws-ui-textarea-input v-model="modelValue" disabled placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="p-4">
				<svws-ui-textarea-input v-model="modelValue" statistics placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from 'histoire/client';
	import { ref } from 'vue';

	const modelValue = ref("This is a filled textarea component");

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>
