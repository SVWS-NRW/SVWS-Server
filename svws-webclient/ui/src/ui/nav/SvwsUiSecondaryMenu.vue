<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>
				<slot name="headline" />
			</h1>
			<div v-if="$slots.abschnitt" class="input--schule-abschnitte">
				<slot name="abschnitt" />
			</div>
		</div>
		<div class="secondary-menu--header">
			<slot name="header" />
		</div>
		<div class="secondary-menu--content">
			<slot name="content" />
		</div>
	</div>
</template>
