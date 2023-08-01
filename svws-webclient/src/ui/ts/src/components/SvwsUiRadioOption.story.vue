<script setup lang="ts">
	import { ref } from "vue"
	const checked = ref('value');
</script>

<template>
	<Story title="Radio Options" id="svws-ui-radio-option" icon="ri:radio-button-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Horizontal Row" id="Horizontal Row">
			<svws-ui-radio-group :row="true">
				<svws-ui-radio-option value="option1" name="radioInputExample" label="Option 1" />
				<svws-ui-radio-option value="option2" name="radioInputExample" label="Option 2" disabled />
				<svws-ui-radio-option value="option3" name="radioInputExample" label="Option 3" />
				<svws-ui-radio-option value="option4" name="radioInputExample" label="Option 4">
					<i-ri-forbid-2-line />
				</svws-ui-radio-option>
				<svws-ui-radio-option value="option4" name="radioInputExample" label="Option 4" :icon="false" />
			</svws-ui-radio-group>
		</Variant>

		<Variant title="Stacked" id="Stacked">
			<svws-ui-radio-group>
				<svws-ui-radio-option value="option1" name="radioInputExample2" label="Option 1" />
				<svws-ui-radio-option value="option2" name="radioInputExample2" label="Option 2" />
			</svws-ui-radio-group>
		</Variant>

		<Variant title="Default" id="Default">
			<div class="flex">
				<svws-ui-radio-option name="name" value="value" label="Option" />
			</div>
		</Variant>

		<Variant title="Checked" id="Checked">
			<div class="flex">
				<svws-ui-radio-option v-model="checked" name="name" value="value" label="Option" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="flex">
				<svws-ui-radio-option name="name" value="value" label="Option" statistics />
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="flex">
				<svws-ui-radio-option name="name" value="value" label="Option" disabled />
			</div>
		</Variant>
	</Story>
</template>
